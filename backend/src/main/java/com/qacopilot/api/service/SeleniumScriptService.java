package com.qacopilot.api.service;

import com.qacopilot.api.dto.SeleniumScriptDTO;

public interface SeleniumScriptService {
    SeleniumScriptDTO generateScript(Long testCaseId);
    SeleniumScriptDTO getScriptByTestCase(Long testCaseId);
}
