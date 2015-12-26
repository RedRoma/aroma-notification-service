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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.aroma.banana.thrift.exceptions.InvalidArgumentException;
import tech.aroma.banana.thrift.notification.service.SendNotificationRequest;
import tech.aroma.banana.thrift.notification.service.SendNotificationResponse;
import tech.aroma.banana.thrift.service.BananaServiceConstants;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class NotificationServiceBaseTest 
{
    
    @Mock
    private ThriftOperation<SendNotificationRequest, SendNotificationResponse> sendNotificationOperation;
    @GeneratePojo
    private SendNotificationRequest sendNotificationRequest;
    @GeneratePojo
    private SendNotificationResponse sendNotificationResponse;

    private NotificationServiceBase instance;
    
    @Before
    public void setUp()
    {
        instance = new NotificationServiceBase(sendNotificationOperation);
        
        verifyZeroInteractions(sendNotificationOperation);
    }
    
    @DontRepeat
    @Test
    public void testConstructor()
    {
        assertThrows(() -> new NotificationServiceBase(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetApiVersion() throws Exception
    {
        double result = instance.getApiVersion();
        assertThat(result, is(BananaServiceConstants.API_VERSION));
    }

    @Test
    public void testSendNotification() throws Exception
    {
        when(sendNotificationOperation.process(sendNotificationRequest))
            .thenReturn(sendNotificationResponse);
        
        SendNotificationResponse response = instance.sendNotification(sendNotificationRequest);
        
        assertThat(response, is(sendNotificationResponse));
        verify(sendNotificationOperation).process(sendNotificationRequest);
        
        //Test with bad arguments
        assertThrows(() -> instance.sendNotification(null))
            .isInstanceOf(InvalidArgumentException.class);
    }
    
    

}