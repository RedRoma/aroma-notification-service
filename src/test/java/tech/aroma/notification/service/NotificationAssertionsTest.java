/*
 * Copyright 2017 RedRoma, Inc.
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.events.Event;
import tech.aroma.thrift.events.EventType;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.aroma.thrift.generators.ChannelGenerators.channels;
import static tech.aroma.thrift.generators.EventGenerators.events;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.TimeGenerators.pastInstants;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.*;

/**
 * @author SirWellington
 */
@Repeat(50)
@RunWith(AlchemyTestRunner.class)
public class NotificationAssertionsTest
{

    private AromaChannel channel;
    private Event event;

    @Before
    public void setUp()
    {
        channel = one(channels());
        event = one(events());
    }

    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        assertThrows(() -> NotificationAssertions.class.newInstance())
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValidAromaChannel()
    {
        AlchemyAssertion<AromaChannel> assertion = NotificationAssertions.validAromaChannel();
        assertThat(assertion, notNullValue());

        assertion.check(channel);

        AromaChannel empty = new AromaChannel();
        assertThrows(() -> assertion.check(empty));
    }

    @Test
    public void testValidEvent()
    {
        AlchemyAssertion<Event> assertion = NotificationAssertions.validEvent();
        checkThat(assertion, notNullValue());


        //Test with Bad arguments
        assertThrows(() -> assertion.check(null))
                .isInstanceOf(FailedAssertionException.class);

        Event empty = new Event();
        assertThrows(() -> assertion.check(empty))
                .isInstanceOf(FailedAssertionException.class);

        //Missing event type
        Event missingEventType = new Event()
                .setTimestamp(one(pastInstants()).toEpochMilli());
        assertThrows(() -> assertion.check(missingEventType))
                .isInstanceOf(FailedAssertionException.class);

        //Remove the timestamp
        event.timestamp = 0;
        assertThrows(() -> assertion.check(event))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testValidEventType()
    {
        AlchemyAssertion<EventType> assertion = NotificationAssertions.validEventType();
        checkThat(assertion, notNullValue());

        EventType eventType = event.eventType;
        assertion.check(eventType);

        // Check with bad arguments

        assertThrows(() -> assertion.check(null))
                .isInstanceOf(FailedAssertionException.class);

        EventType empty = new EventType();
        assertThrows(() -> assertion.check(empty))
                .isInstanceOf(FailedAssertionException.class);

    }

}
