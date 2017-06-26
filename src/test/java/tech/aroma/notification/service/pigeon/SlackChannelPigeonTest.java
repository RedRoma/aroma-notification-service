/*
 * Copyright 20167 RedRoma, Inc.
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

package tech.aroma.notification.service.pigeon;

import java.net.MalformedURLException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.aroma.thrift.channels.SlackChannel;
import tech.aroma.thrift.events.Event;
import tech.sirwellington.alchemy.http.AlchemyHttp;
import tech.sirwellington.alchemy.http.mock.AlchemyHttpMock;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static tech.aroma.thrift.generators.EventGenerators.events;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;


/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class SlackChannelPigeonTest 
{
    
    private AlchemyHttp http;
    private final String url  = SlackChannelPigeon.SLACK_API_URL;
    
    private SlackChannelPigeon instance;
    
    @GeneratePojo
    private SlackChannelPigeon.SlackResponse response;
    
    @GeneratePojo
    private SlackChannel channel;
    
    private Event event;
    
    @Before
    public void setUp() throws MalformedURLException
    {
        http = AlchemyHttpMock.begin()
            .whenPost()
            .anyBody()
            .at(url)
            .thenReturnPOJO(response)
            .build();
        
        http = spy(http);
        
        instance = new SlackChannelPigeon(http);
        verifyZeroInteractions(http);
        
        event = one(events());
    }

    @Test
    public void testDeliverMessageTo()
    {
        instance.deliverMessageTo(event, channel);
        
        AlchemyHttpMock.verifyAllRequestsMade(http);
    }

}
