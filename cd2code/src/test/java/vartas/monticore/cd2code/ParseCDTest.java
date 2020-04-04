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

package vartas.monticore.cd2code;

import org.junit.jupiter.api.Test;

public class ParseCDTest extends BasicCDTest{
    @Test
    public void testParseCache(){
        parseCDClass("Browser","vartas","monticore","cd2code","BrowserCD");
    }

    @Test
    public void testParseInstant(){
        parseCDClass("Person","vartas","monticore","cd2code","PersonCD");
    }

    @Test
    public void testParseURL(){
        parseCDClass("Website","vartas","monticore","cd2code","InternetCD");
    }

    @Test
    public void testParseParameter(){
        parseCDClass("Database","vartas","monticore","cd2code","DatabaseCD");
    }
}
