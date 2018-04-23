package com.cronutils.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cronutils.mapper.CronMapper;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.utils.Preconditions;

public class CompositeCron implements Cron {

    private static final long serialVersionUID = 1L;
    private final Pattern split = Pattern.compile("\\|");
    private final List<Cron> crons;
    private final CronDefinition definition;

    public CompositeCron(final List<Cron> crons){
        this.crons = Collections.unmodifiableList(crons);
        Preconditions.checkNotNullNorEmpty(crons, "List of Cron cannot be null or empty");
        definition = crons.get(0).getCronDefinition();
        Preconditions.checkArgument(crons.size() == crons.stream().filter(c -> c.getCronDefinition().equals(definition)).count(), "All Cron objects must have same definition for CompositeCron");
    }

    public List<Cron> getCrons() {
        return crons;
    }

    @Override
    public CronField retrieve(final CronFieldName name) {
        throw new UnsupportedOperationException("Currently not supported for CompositeCron");
    }

    @Override
    public Map<CronFieldName, CronField> retrieveFieldsAsMap() {
        throw new UnsupportedOperationException("Currently not supported for CompositeCron");
    }

    @Override
    public String asString() {
        final StringBuilder builder = new StringBuilder();
        final List<String> patterns = crons.stream().map(Cron::asString).collect(Collectors.toList());
        final int fields = patterns.get(0).split(" ").length;
        for (int j = 0; j < fields; j++) {
            final StringBuilder fieldbuilder = new StringBuilder();
            for (final String pattern : patterns) {
                fieldbuilder.append(String.format("%s ", pattern.split(" ")[j]));
            }
            String fieldstring = fieldbuilder.toString().trim().replaceAll(" ", "|");
            if (split.splitAsStream(fieldstring).distinct().limit(2).count() <= 1) {
                fieldstring = split.split(fieldstring)[0];
            }
            builder.append(String.format("%s ", fieldstring));
        }
        return builder.toString().trim();
    }

    @Override
    public CronDefinition getCronDefinition() {
        return definition;
    }

    @Override
    public Cron validate() {
        for (final Cron cron : crons) {
            cron.validate();
        }
        return this;
    }

    @Override
    public boolean equivalent(final CronMapper cronMapper, final Cron cron) {
        throw new UnsupportedOperationException("Currently not supported for CompositeCron");
    }

    @Override
    public boolean equivalent(final Cron cron) {
        return asString().equals(cron.asString());
    }
}
