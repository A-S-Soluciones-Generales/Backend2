package com.ays.kardex.service;

import com.ays.kardex.dto.sede.SedeRequest;
import com.ays.kardex.dto.sede.SedeResponse;

public interface SedeService {

    SedeResponse crear(SedeRequest request);
}
