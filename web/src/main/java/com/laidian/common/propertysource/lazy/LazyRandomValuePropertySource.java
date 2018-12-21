package com.laidian.common.propertysource.lazy;

import com.laidian.common.util.NetUtil;
import com.laidian.common.util.NumberUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * lazy init random value and same expression will only init once and return same value by calling any times
 */
public class LazyRandomValuePropertySource extends PropertySource<Random> {

    private final Map<String, Object> cache = new ConcurrentHashMap<>(16);

    //默认过滤器：恒为真，即不做过滤
    private Predicate filter = (t) -> true;

    public static final int MAX_RETRY = 2000;


    /**
     * Name of the lazy random {@link PropertySource}.
     */
    public static final String LAZY_RANDOM_PROPERTY_SOURCE_NAME = "lazy.random";

    private static final String PREFIX = "lazy.random.";

    private static final Log logger = LogFactory.getLog(RandomValuePropertySource.class);

    public LazyRandomValuePropertySource(String name, Predicate filter) {
        super(name, new Random());
        if (filter != null) {
            this.filter = filter;
        }
    }

    public LazyRandomValuePropertySource(String name) {
        this(name, null);
    }

    public LazyRandomValuePropertySource() {
        this(LAZY_RANDOM_PROPERTY_SOURCE_NAME);
    }

    /**
     * todo 分组支持 maybe or never?
     *  是否支持随机数分组，目前的策略是同一范围内(随机数类型和范围相同)只会生成一次随机值
     *
     * @param name
     * @return
     */
    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(PREFIX)) {
            return null;
        }
        System.out.println("Generating lazy random property for '" + name + "'");
        if (logger.isTraceEnabled()) {
            logger.trace("Generating lazy random property for '" + name + "'");
        }
        return getRandomValue(name.substring(PREFIX.length()));
    }

    private Object getFilteredResult(Supplier supplier) {
        for (int i = 1; i <= MAX_RETRY; i++) {
            System.out.println("retry times:====" + i);
            Object result = supplier.get();
            if (filter.test(result)) {
                return result;
            }
        }
        //在最大尝试次数用尽后仍未找到可用结果，则抛出异常，应用启动失败[在某一范围内随机取值2000次，可能某些值无法取到，不代表范围内所有数据都不可用]
        throw new RuntimeException("there is no port can be used,after random try "+MAX_RETRY+" times,please increase the port range");
    }

    private Object getRandomValue(String type) {
        if (type.equals("int")) {
            return cache.computeIfAbsent("int", (key) -> getFilteredResult(() -> getSource().nextInt()));
        }
        if (type.equals("long")) {
            return cache.computeIfAbsent("long", (key) -> getFilteredResult(() -> getSource().nextLong()));
        }
        String range = getRange(type, "int");
        if (range != null) {
            String finalRange = range;
            return cache.computeIfAbsent("int-range-" + finalRange, (key) -> getFilteredResult(() -> getNextIntInRange(finalRange)));
        }
        range = getRange(type, "long");
        if (range != null) {
            String finalRange = range;
            return cache.computeIfAbsent("long-range-" + finalRange, (key) -> getFilteredResult(() -> getNextLongInRange(finalRange)));
        }
        if (type.equals("uuid")) {
            return cache.computeIfAbsent("uuid", (key) -> getFilteredResult(() -> UUID.randomUUID().toString()));
        }
        return cache.computeIfAbsent("bytes", (key) -> getFilteredResult(() -> getRandomBytes()));
    }

    private String getRange(String type, String prefix) {
        if (type.startsWith(prefix)) {
            int startIndex = prefix.length() + 1;
            if (type.length() > startIndex) {
                return type.substring(startIndex, type.length() - 1);
            }
        }
        return null;
    }

    private int getNextIntInRange(String range) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(range);
        int start = Integer.parseInt(tokens[0]);
        if (tokens.length == 1) {
            return getSource().nextInt(start);
        }
        return start + getSource().nextInt(Integer.parseInt(tokens[1]) - start);
    }

    private long getNextLongInRange(String range) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(range);
        if (tokens.length == 1) {
            return Math.abs(getSource().nextLong() % Long.parseLong(tokens[0]));
        }
        long lowerBound = Long.parseLong(tokens[0]);
        long upperBound = Long.parseLong(tokens[1]) - lowerBound;
        return lowerBound + Math.abs(getSource().nextLong() % upperBound);
    }

    private Object getRandomBytes() {
        byte[] bytes = new byte[32];
        getSource().nextBytes(bytes);
        return DigestUtils.md5DigestAsHex(bytes);
    }

    @NotNull
    @Contract(" -> new")
    public static PropertySource getLazyRandomPortPropertySource() {
        return new LazyRandomValuePropertySource(LAZY_RANDOM_PROPERTY_SOURCE_NAME, (value) -> {
            if (value != null && NumberUtil.isNumeric(value.toString())) {
                Integer port = Integer.valueOf(value.toString());
                //注意此处会打开一个socket进行端口检测，有资源损耗
                if (NetUtil.isPortInUse(port)) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        });
    }

    public static void addDefaultLazyRandomPSToEnvironment(@NotNull ConfigurableEnvironment environment) {
        environment.getPropertySources().addLast(new LazyRandomValuePropertySource(LAZY_RANDOM_PROPERTY_SOURCE_NAME));
        logger.trace("LazyRandomValuePropertySource add to Environment");
    }

    public static void addLazyRandomPortPSToEnvironment(@NotNull ConfigurableEnvironment environment) {
        environment.getPropertySources().addLast(getLazyRandomPortPropertySource());
        logger.trace("LazyRandomPortPropertySource add to Environment");
    }


}
