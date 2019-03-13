package com.example.demo.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

    @Value("${mongodb.multidb.uri1}")
    private String uri1;

    @Value("${mongodb.multidb.uri2}")
    private String uri2;

    public String getUri1() {
        return uri1;
    }

    public void setUri1(String uri1) {
        this.uri1 = uri1;
    }

    public String getUri2() {
        return uri2;
    }

    public void setUri2(String uri2) {
        this.uri2 = uri2;
    }

    /*public MongoDbFactory mongoDbFactory(String uri) {
        List<ServerAddress> seeds = new ArrayList<>();
        String[] hostPorts = host.split(",");
        for(int i = 0 ; i < hostPorts.length;i++){
            String[] hps = hostPorts[i].split(":");
            ServerAddress serverAddress = new ServerAddress(hps[0], Integer.valueOf(hps[1]));
            seeds.add(serverAddress);
        }
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new SimpleMongoDbFactory(new MongoClient(seeds), database);
        }

        //MongoOptions
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(MongoCredential.createCredential(username, database, password.toCharArray()));
        return new SimpleMongoDbFactory(new MongoClient(seeds, mongoCredentialList, null), database);
        return new SimpleMongoDbFactory(new MongoClientURI(uri));
    }*/

    @Bean(name = "mongoTemplate1")
    @Primary
    public MongoTemplate mongoTemplate1(){
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(uri1)));
    }

    @Bean(name = "mongoTemplate2")
    public MongoTemplate mongoTemplate2(){
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(uri2)));
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        } catch (NoSuchBeanDefinitionException ignore) {
        }

        // Don't save _class to mongo
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }

}