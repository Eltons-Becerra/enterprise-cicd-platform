package com.enterprise.platform.service;

package com.enterprise.platform.service;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;

public interface PlatformService {

    HealthResponse health();

    ServiceInfoResponse info();

}