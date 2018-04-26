package com.cronutils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.Test;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

public class Issue324Test {


    @Test
    public void test() {
        final CronDefinition cronDefinition =
                CronDefinitionBuilder.defineCron()
                .withSeconds().and()
                .withMinutes().and()
                .withHours().and()
                .withDayOfMonth()
                .supportsHash().supportsL().supportsW().and()
                .withMonth().and()
                .withDayOfWeek()
                .withIntMapping(7, 0)
                .supportsHash().supportsL().supportsW().and()
                .withYear().optional().and()
                .instance();
                final CronParser parser = new CronParser(cronDefinition);

                    final ZoneId zoneIdJo = ZoneId.of("Africa/Johannesburg");
                    final ZoneId zoneIdCairo = ZoneId.of("Africa/Cairo");
                    final ZoneId[] zoneIds = new ZoneId[] {zoneIdJo, zoneIdCairo};
                    final Cron cron = parser.parse("0 0 0 * * *");
                    final ExecutionTime executionTime = ExecutionTime.forCron(cron);
                    System.out.println("Testing cron: '" + CronDescriptor.instance().describe(cron) + "'");
                    for (final ZoneId zoneId : zoneIds) {
                        final ZonedDateTime reference = ZonedDateTime.of(LocalDateTime.of(2018, 4, 27, 23, 0), zoneId);
                        System.out.println("reference => " + reference);

                        final Optional<ZonedDateTime> next = executionTime.nextExecution(reference);
                        System.out.println("next execution after reference=> " + next.get());

                        System.out.println("next call has bug for Cairo works for Joburg");
                        final Optional<ZonedDateTime> previous = executionTime.lastExecution(reference);
                        System.out.println("previous execution before reference=> " + previous.get());
                    }

    }
}
