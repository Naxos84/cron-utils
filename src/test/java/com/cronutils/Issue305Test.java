package com.cronutils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;

import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.every;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Ignore
public class Issue305Test {
    @Test
    public void test() {
        final CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
        final CronBuilder cb = CronBuilder.cron(cronDefinition)
                .withYear(every(on(2015), 2))
                .withMonth(on(8))
                .withDoM(on(15))
                .withDoW(questionMark());
        final Cron cron = cb.instance();
        final ExecutionTime executionTime = ExecutionTime.forCron(cron);

        final Set<ZonedDateTime> dates = new LinkedHashSet<>();

        final List<String> expected = new ArrayList<>();
        expected.add("2015-08-15T00:00Z[UTC]");
        expected.add("2017-08-15T00:00Z[UTC]");
        expected.add("2019-08-15T00:00Z[UTC]");

        Optional<ZonedDateTime> nextExecution =
                executionTime.nextExecution(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")));

        final List<String> computed = new ArrayList<>();
        if (nextExecution.isPresent()) {
            while (!nextExecution.get().isAfter(ZonedDateTime.of(2020, 12, 31, 0, 0, 0, 0, ZoneId.of("UTC")))) {
                computed.add(nextExecution.get().toString());
                dates.add(nextExecution.get());
                nextExecution = executionTime.nextExecution(nextExecution.get());
            }
        } else {
            fail("next execution was not present.");
        }
        for (int j = 0; j < expected.size(); j++) {
            assertEquals(expected.get(j), computed.get(j));
        }
    }
}
