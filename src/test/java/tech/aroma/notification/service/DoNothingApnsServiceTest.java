/*
 * Copyright 2016 RedRoma, Inc..
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
import java.util.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.sirwellington.alchemy.generator.BinaryGenerators;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.GenerateString;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.HEXADECIMAL;

/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class DoNothingApnsServiceTest 
{

    @GenerateString(HEXADECIMAL)
    private String deviceToken;
    
    @GenerateString(ALPHABETIC)
    private String message;
    
    private byte[] binary;
    
    @Mock
    private ApnsNotification notification;
    
    private DoNothingApnsService instance;
    
    @Before
    public void setUp() throws Exception
    {
        
        setupData();
        setupMocks();
        
        instance = new DoNothingApnsService();
    }


    private void setupData() throws Exception
    {
        binary = BinaryGenerators.binary(1000).get();
    }

    private void setupMocks() throws Exception
    {
        
    }

    @Test
    public void testPush_String_String()
    {
        instance.push(deviceToken, message);
    }

    @Test
    public void testPush_3args_1()
    {
    }

    @Test
    public void testPush_byteArr_byteArr()
    {
        instance.push(binary, binary);
    }

    @Test
    public void testPush_3args_2()
    {
    }

    @Test
    public void testPush_Collection_String()
    {
    }

    @Test
    public void testPush_3args_3()
    {
    }

    @Test
    public void testPush_Collection_byteArr()
    {
    }

    @Test
    public void testPush_3args_4()
    {
    }

    @Test
    public void testPush_ApnsNotification()
    {
        instance.push(notification);
    }

    @Test
    public void testStart()
    {
        instance.start();
    }

    @Test
    public void testStop()
    {
        instance.stop();
    }

    @Test
    public void testGetInactiveDevices()
    {
        Map<String, Date> result = instance.getInactiveDevices();
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void testTestConnection()
    {
        instance.testConnection();
    }

}