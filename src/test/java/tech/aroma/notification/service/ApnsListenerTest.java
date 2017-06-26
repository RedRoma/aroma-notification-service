/*
 * Copyright 20167 RedRoma, Inc..
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

package tech.aroma.notification.service;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.BooleanGenerators.booleans;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;


/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class ApnsListenerTest 
{
   
    @Mock
    private ApnsNotification notification;
    
    private ApnsListener instance;
    private boolean resent;
    
    @Before
    public void setUp() throws Exception
    {
        
        setupData();
        setupMocks();
        
        instance = new ApnsListener();
    }


    private void setupData() throws Exception
    {
        resent = one(booleans());
    }

    private void setupMocks() throws Exception
    {
        
    }

    @Test
    public void testMessageSent()
    {
        instance.messageSent(notification, resent);
    }

    @Test
    public void testMessageSendFailed()
    {
        Throwable ex = new Exception();
        instance.messageSendFailed(notification, ex);
    }

    @Test
    public void testConnectionClosed()
    {
        int code = one(integers(0, 10));
        DeliveryError error = enumValueOf(DeliveryError.class).get();
        instance.connectionClosed(error, code);
    }

    @Test
    public void testCacheLengthExceeded()
    {
        int length = one(integers(1000, 100_000));
        instance.cacheLengthExceeded(length);
    }

    @Test
    public void testNotificationsResent()
    {
        int count = one(integers(1, 100));
        instance.notificationsResent(count);
    }

}