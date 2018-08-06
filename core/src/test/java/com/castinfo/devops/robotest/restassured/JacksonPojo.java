package com.castinfo.devops.robotest.restassured;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "echo" })
public class JacksonPojo {
    @JsonProperty("echo")
    private String echo = null;

    @JsonProperty("echo")
    public String getEcho() {
        return echo;
    }

    @JsonProperty("echo")
    public void setEcho(final String echo) {
        this.echo = echo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(echo)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JacksonPojo)) {
            return false;
        }
        JacksonPojo rhs = (JacksonPojo) other;
        return new EqualsBuilder().append(echo,
                                          rhs.echo)
                                  .isEquals();
    }
}
