package io.micronaut.servlet.undertow;

import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.core.bind.annotation.Bindable;
import io.micronaut.core.convert.format.MapFormat;
import io.micronaut.core.naming.conventions.StringConvention;
import io.micronaut.http.server.HttpServerConfiguration;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;

import javax.annotation.Nullable;
import javax.servlet.MultipartConfigElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration for the Undertow server.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@ConfigurationProperties("undertow")
@TypeHint(
        value = {UndertowOptions.class, org.xnio.Option.class},
        accessType = TypeHint.AccessType.ALL_DECLARED_FIELDS
)
public class UndertowConfiguration extends HttpServerConfiguration {

    @ConfigurationBuilder
    protected Undertow.Builder undertowBuilder = Undertow.builder();

    private final MultipartConfiguration multipartConfiguration;
    private Map<String, String> workerOptions = new HashMap<>(5);
    private Map<String, String> socketOptions = new HashMap<>(5);
    private Map<String, String> serverOptions = new HashMap<>(5);

    /**
     * Default constructor.
     * @param multipartConfiguration The multipart configuration
     */
    public UndertowConfiguration(@Nullable MultipartConfiguration multipartConfiguration) {
        this.multipartConfiguration = multipartConfiguration;
    }


    /**
     * @return The undertow builder
     */
    public Undertow.Builder getUndertowBuilder() {
        return undertowBuilder;
    }

    /**
     * @return The multipart configuration
     */
    public Optional<MultipartConfiguration> getMultipartConfiguration() {
        return Optional.ofNullable(multipartConfiguration);
    }

    /**
     * @return The worker options
     */
    public Map<String, String> getWorkerOptions() {
        return workerOptions;
    }

    /**
     * Sets the worker options.
     * @param workerOptions The worker options
     */
    public void setWorkerOptions(
            @MapFormat(keyFormat = StringConvention.UNDER_SCORE_SEPARATED,
                       transformation = MapFormat.MapTransformation.FLAT)
            Map<String, String> workerOptions) {
        if (workerOptions != null) {
            this.workerOptions.putAll(workerOptions);
        }
    }

    /**
     * @return The socket options.
     */
    public Map<String, String> getSocketOptions() {
        return socketOptions;
    }

    /**
     * Sets the socket options.
     * @param socketOptions The socket options
     */
    public void setSocketOptions(
            @MapFormat(keyFormat = StringConvention.UNDER_SCORE_SEPARATED,
                       transformation = MapFormat.MapTransformation.FLAT)
            Map<String, String> socketOptions) {
        if (socketOptions != null) {
            this.socketOptions.putAll(socketOptions);
        }
    }

    /**
     * @return The server options.
     */
    public Map<String, String> getServerOptions() {
        return serverOptions;
    }

    /**
     * @param serverOptions Sets the server options
     */
    public void setServerOptions(
            @MapFormat(keyFormat = StringConvention.UNDER_SCORE_SEPARATED,
                       transformation = MapFormat.MapTransformation.FLAT)
            Map<String, String> serverOptions) {
        if (serverOptions != null) {
            this.serverOptions.putAll(serverOptions);
        }
    }

    /**
     * The multipart configuration.
     */
    @ConfigurationProperties("multipart")
    public static class MultipartConfiguration extends MultipartConfigElement {

        /**
         * Default constructor.
         * @param location The location
         * @param maxFileSize The file size
         * @param maxRequestSize The max request size
         * @param fileSizeThreshold The threshold
         */
        @ConfigurationInject
        public MultipartConfiguration(
                @Nullable String location,
                @Bindable(defaultValue = "-1") long maxFileSize,
                @Bindable(defaultValue = "-1") long maxRequestSize,
                @Bindable(defaultValue = "0") int fileSizeThreshold) {
            super(location, maxFileSize, maxRequestSize, fileSizeThreshold);
        }
    }
}
