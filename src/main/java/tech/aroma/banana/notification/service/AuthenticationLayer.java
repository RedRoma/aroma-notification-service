/*
 * Copyright 2015 Aroma Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 
package tech.aroma.banana.notification.service;


import javax.inject.Inject;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.banana.thrift.authentication.service.AuthenticationService;
import tech.aroma.banana.thrift.exceptions.InvalidArgumentException;
import tech.aroma.banana.thrift.exceptions.InvalidTokenException;
import tech.aroma.banana.thrift.exceptions.OperationFailedException;
import tech.aroma.banana.thrift.notification.service.NotificationService;
import tech.aroma.banana.thrift.notification.service.SendNotificationRequest;
import tech.aroma.banana.thrift.notification.service.SendNotificationResponse;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern;

import static tech.aroma.banana.thrift.assertions.BananaAssertions.checkRequestNotNull;
import static tech.aroma.banana.thrift.assertions.BananaAssertions.legalToken;
import static tech.aroma.banana.thrift.assertions.BananaAssertions.validTokenIn;
import static tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern.Role.CONCRETE_DECORATOR;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 * This class decorates an existing {@linkplain NotificationService.Iface Notification Service},
 * and adds Authentication of Tokens against an {@linkplain AuthenticationService.Iface Authentication Service}.
 * 
 * @author SirWellington
 */
@Internal
@DecoratorPattern(role = CONCRETE_DECORATOR)
final class AuthenticationLayer implements NotificationService.Iface
{
    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationLayer.class);

    private final AuthenticationService.Iface authenticationService;
    private final NotificationService.Iface delegate;
    
    @Inject
    AuthenticationLayer(AuthenticationService.Iface authenticationService,
                        NotificationService.Iface delegate)
    {
        checkThat(authenticationService, delegate)
            .are(notNull());

        this.authenticationService = authenticationService;
        this.delegate = delegate;
    }

    
    
    @Override
    public double getApiVersion() throws TException
    {
        return delegate.getApiVersion();
    }

    @Override
    public SendNotificationResponse sendNotification(SendNotificationRequest request) throws InvalidArgumentException,
                                                                                             OperationFailedException,
                                                                                             InvalidTokenException, 
                                                                                             TException
    {
        checkRequestNotNull(request);
        
        checkThat(request.token)
            .throwing(InvalidTokenException.class)
            .is(legalToken())
            .is(validTokenIn(authenticationService));
        
        return delegate.sendNotification(request);
    }

}
