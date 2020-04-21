/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.monticore.cd4java;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSV2StringArray extends SimpleArgumentConverter {

    private static final Pattern PATTERN = Pattern.compile("\\w+(<.+>)?(\\[])?");

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        Matcher matcher = PATTERN.matcher(source.toString());
        List<String> matches = new ArrayList<>();

        while(matcher.find())
            matches.add(matcher.group());

        return matches.toArray(String[]::new);
    }
}
