/*
 * Copyright 2016 RedRoma.
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

package tech.aroma.notification.service.serialization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.events.Event;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;
import tech.sirwellington.alchemy.thrift.ThriftObjects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static tech.aroma.notification.service.ChannelGenerators.channels;
import static tech.aroma.notification.service.EventGenerators.events;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;

/**
 *
 * @author SirWellington
 */
@Repeat(50)
@RunWith(AlchemyTestRunner.class)
public class SerializationTest
{

    private Event event;
    private AromaChannel channel;

    @Before
    public void setUp() throws Exception
    {

        setupData();
    }

    private void setupData() throws Exception
    {
        event = one(events());
        channel = one(channels());
    }

    @Test
    public void testChannelBinary() throws Exception
    {
        byte[] binary = ThriftObjects.toBinary(event);
        Event result = ThriftObjects.fromBinary(new Event(), binary);
        assertThat(result, is(event));
    }

    @Test
    public void testChannelJson() throws Exception
    {
        String eventJson = ThriftObjects.toJson(event);
        Event result = ThriftObjects.fromJson(new Event(), eventJson);
        assertThat(result, is(event));
    }

    @Test
    public void testEventBinary() throws Exception
    {
        byte[] binary = ThriftObjects.toBinary(channel);
        AromaChannel result = ThriftObjects.fromBinary(new AromaChannel(), binary);
        assertThat(result, is(channel));
    }
    
    @Test
    public void testEventJson() throws Exception
    {
        String channelJson = ThriftObjects.toJson(channel);
        AromaChannel result = ThriftObjects.fromJson(new AromaChannel(), channelJson);
        assertThat(result, is(channel));
    }
    
}
