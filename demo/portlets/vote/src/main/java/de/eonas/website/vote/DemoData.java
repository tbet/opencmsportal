package de.eonas.website.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DemoData {
    @Autowired
    Dao dao;

    @PostConstruct
    public void insertDemoData() {
        dao.insertDemoDataTransacted();
    }
}
