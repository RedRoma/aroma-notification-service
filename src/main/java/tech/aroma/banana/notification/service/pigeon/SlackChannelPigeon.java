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

import com.google.gson.JsonObject;
import java.util.Objects;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.banana.thrift.channels.SlackChannel;
import tech.aroma.banana.thrift.notifications.Event;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;
import tech.sirwellington.alchemy.http.AlchemyHttp;

import static tech.aroma.banana.notification.service.NotificationAssertions.validEvent;
import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 * This Pigeon delivers messages to a Slack Channel.
 *
 * @author SirWellington
 */
@Internal
@StrategyPattern(role = CONCRETE_BEHAVIOR)
final class SlackChannelPigeon implements Pigeon<SlackChannel>
{

    private final static Logger LOG = LoggerFactory.getLogger(SlackChannelPigeon.class);

    static final String SLACK_API_URL = "https://slack.com/api/chat.postMessage";

    private final AlchemyHttp http;

    @Inject
    SlackChannelPigeon(AlchemyHttp http)
    {
        checkThat(http).is(notNull());

        this.http = http;
    }

    @Override
    public void deliverMessageTo(Event message, SlackChannel channel)
    {
        checkThat(message, channel).are(notNull());

        checkThat(message).is(validEvent());

        checkThat(channel.channelName, channel.domainName, channel.slackToken)
            .usingMessage("Slack Channel missing properties")
            .are(nonEmptyString());

        SlackRequest request = new SlackRequest();
        request.token = channel.channelName;
        request.channel = channel.channelName;
        request.text = message.toString();

        LOG.info("Sending request to slack: {}", request);

        SlackResponse response;
        try
        {
            response = http.go()
                .post()
                .body(request)
                .expecting(SlackResponse.class)
                .at(SLACK_API_URL);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to send message to Slack: {}", message, ex);
            return;
        }
        
        LOG.info("Successfully posted message to Slack: {}", response);
    }

    static class SlackRequest
    {

        private String token;
        private String channel;
        private String text;
        private String username;

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 23 * hash + Objects.hashCode(this.token);
            hash = 23 * hash + Objects.hashCode(this.channel);
            hash = 23 * hash + Objects.hashCode(this.text);
            hash = 23 * hash + Objects.hashCode(this.username);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final SlackRequest other = (SlackRequest) obj;
            if (!Objects.equals(this.token, other.token))
            {
                return false;
            }
            if (!Objects.equals(this.channel, other.channel))
            {
                return false;
            }
            if (!Objects.equals(this.text, other.text))
            {
                return false;
            }
            if (!Objects.equals(this.username, other.username))
            {
                return false;
            }
            return true;
        }

        @Override
        public String toString()
        {
            return "SlackRequest{" + "token=" + token + ", channel=" + channel + ", text=" + text + ", username=" + username + '}';
        }

    }

    static class SlackResponse
    {

        private String ok;
        private String ts;
        private JsonObject message;

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.ok);
            hash = 29 * hash + Objects.hashCode(this.ts);
            hash = 29 * hash + Objects.hashCode(this.message);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final SlackResponse other = (SlackResponse) obj;
            if (!Objects.equals(this.ok, other.ok))
            {
                return false;
            }
            if (!Objects.equals(this.ts, other.ts))
            {
                return false;
            }
            if (!Objects.equals(this.message, other.message))
            {
                return false;
            }
            return true;
        }

        @Override
        public String toString()
        {
            return "SlackResponse{" + "ok=" + ok + ", ts=" + ts + ", message=" + message + '}';
        }

    }

}
