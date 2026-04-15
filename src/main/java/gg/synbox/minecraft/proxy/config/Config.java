package gg.synbox.minecraft.proxy.config;

import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {
    private final Path dataDirectory;
    private final Logger logger;

    private String apiKey;
    private String organization;
    private int refreshInterval;
    private int port;

    public Config(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public void load() {
        Path configPath = dataDirectory.resolve("config.yml");
        
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            
            if (!Files.exists(configPath)) {
                saveDefaultConfig(configPath);
            }
            
            Yaml yaml = new Yaml();
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                Map<String, Object> data = yaml.load(inputStream);
                
                this.apiKey = (String) data.getOrDefault("api_key", null);
                this.organization = (String) data.getOrDefault("organization", null);
                this.refreshInterval = (Integer) data.getOrDefault("refresh_interval", 300);
                this.port = (Integer) data.getOrDefault("port", 25657);
            }
            
            logger.info("Config loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load config", e);
        }
    }

    private void saveDefaultConfig(Path configPath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/config.yml")) {
            if (inputStream != null) {
                try (OutputStream outputStream = Files.newOutputStream(configPath)) {
                    inputStream.transferTo(outputStream);
                }
                logger.info("Default config created at {}", configPath);
            }
        }
    }

    public int getPort() {
        return port;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getOrganization() {
        return organization;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }
}
