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

package tech.aroma.notification.service.operations;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import tech.aroma.notification.service.pigeon.Pigeon;
import tech.aroma.notification.service.pigeon.PigeonFactory;
import tech.aroma.thrift.User;
import tech.aroma.thrift.authentication.AuthenticationToken;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.aroma.thrift.notification.service.SendNotificationRequest;
import tech.aroma.thrift.notification.service.SendNotificationResponse;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static tech.aroma.notification.service.ChannelGenerators.channels;
import static tech.aroma.notification.service.EventGenerators.events;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.ObjectGenerators.pojos;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class SendNotificationOperationTest
{

    @Mock
    private PigeonFactory pigeonFactory;

    @Mock
    private Pigeon pigeon;

    @GeneratePojo
    private AuthenticationToken token;
    
    private SendNotificationRequest request;

    private SendNotificationOperation instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new SendNotificationOperation(pigeonFactory);

        verifyZeroInteractions(pigeonFactory);

        setupData();
    }

    private void setupData() throws Exception
    {
        AlchemyGenerator<User> users = pojos(User.class);

        Map<AromaChannel, User> channels = listOf(channels())
            .stream()
            .distinct()
            .collect(toMap(c -> c, c -> users.get()));

        request = new SendNotificationRequest()
            .setToken(token)
            .setChannels(channels)
            .setEvent(one(events()));
    }

    @DontRepeat
    @Test
    public void testConstructor()
    {
        assertThrows(() -> new SendNotificationOperation(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testProcess() throws Exception
    {
        SendNotificationResponse response = instance.process(request);

        assertThat(response, notNullValue());

        verify(pigeonFactory, times(request.channels.size()))
            .getPigeonFor(Mockito.any());
    }
    
    @DontRepeat
    @Test
    public void testWithNoChannels() throws Exception
    {
        SendNotificationRequest noChannels = new SendNotificationRequest()
            .setToken(token)
            .setEvent(request.event);
        
        SendNotificationResponse response = instance.process(noChannels);
        assertThat(response, notNullValue());
    }

    @DontRepeat
    @Test
    public void testProcessWithBadRequest() throws Exception
    {
        assertThrows(() -> instance.process(null))
            .isInstanceOf(InvalidArgumentException.class);

        SendNotificationRequest empty = new SendNotificationRequest();

        assertThrows(() -> instance.process(empty))
            .isInstanceOf(InvalidArgumentException.class);

        SendNotificationRequest noEvent = new SendNotificationRequest()
            .setChannels(request.channels);

        assertThrows(() -> instance.process(noEvent))
            .isInstanceOf(InvalidArgumentException.class);

        


    }

}
