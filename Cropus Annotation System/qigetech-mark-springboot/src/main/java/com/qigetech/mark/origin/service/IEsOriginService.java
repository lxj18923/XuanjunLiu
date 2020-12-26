package com.qigetech.mark.origin.service;

import java.util.Map;

public interface IEsOriginService {
    boolean updateStatusByEs(int originId, int status);
    Map<String, Object> findByStatusOrSource(long current, long size, Integer status, String source, String sentence);

}

