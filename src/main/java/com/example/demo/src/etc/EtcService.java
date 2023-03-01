package com.example.demo.src.etc;

import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtcService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EtcProvider etcProvider;
    private final EtcDao etcDao;
    private final JwtService jwtService;
    @Autowired
    public EtcService(EtcProvider etcProvider, EtcDao etcDao, JwtService jwtService) {
        this.etcProvider = etcProvider;
        this.etcDao = etcDao;
        this.jwtService = jwtService;
    }
}