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


package tech.aroma.banana.notification.service.pigeon;

import org.apache.thrift.TBase;
import tech.aroma.banana.thrift.channels.BananaChannel;
import tech.aroma.banana.thrift.events.Event;
import tech.aroma.banana.thrift.exceptions.OperationFailedException;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;


/**
 * A Pigeon is responsible for sending a Message to a particular {@link BananaChannel}.
 * 
 * @author SirWellington
 * 
 * @param <C> The particular {@link BananaChannel} an implementation delivers.
 */
@StrategyPattern(role = INTERFACE)
public interface Pigeon <C extends TBase>
{
    /**
     * Delivers a message to a particular {@link BananaChannel}.
     * 
     * @param message
     * @param channel
     * 
     * @throws OperationFailedException If the message could not be delivered.
     */
    void deliverMessageTo(Event message, C channel) throws OperationFailedException;
}
