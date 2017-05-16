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


package tech.aroma.notification.service.operations;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.notification.service.pigeon.Pigeon;
import tech.aroma.notification.service.pigeon.PigeonFactory;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.notification.service.SendNotificationRequest;
import tech.aroma.thrift.notification.service.SendNotificationResponse;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

/**
 * @author SirWellington
 */
public final class ModuleNotificationOperations extends AbstractModule
{
    private final static Logger LOG = LoggerFactory.getLogger(ModuleNotificationOperations.class);

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<ThriftOperation<SendNotificationRequest,SendNotificationResponse>>() {})
            .to(SendNotificationOperation.class);
    }

    @Provides
    PigeonFactory providePigeonFactory()
    {
        return new PigeonFactory()
        {
            @Override
            public <C extends TBase> Pigeon<C> getPigeonFor(AromaChannel bananaChannel) throws IllegalArgumentException
            {
                return null;
            }
        };
    }

}
