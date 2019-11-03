/*
 * Copyright (c) 2019 Zavarov
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

package vartas.discord.bot.rank._symboltable;

import de.se_rwth.commons.logging.Log;

import java.util.Optional;

//#TODO This is a temporary fix until the infinite recursive call in MontiCore is fixed.
public class RankScope extends RankScopeTOP{
    public RankScope() {
        super();
        this.name = Optional.empty();
    }

    public RankScope(boolean isShadowingScope) {
        this.shadowing = isShadowingScope;
        this.name = Optional.empty();
    }

    public RankScope(IRankScope enclosingScope) {
        this(enclosingScope, false);
    }

    public RankScope(IRankScope enclosingScope, boolean isShadowingScope) {
        this.setEnclosingScope(enclosingScope);
        this.shadowing = isShadowingScope;
        this.name = Optional.empty();
    }
    @Override
    public void setEnclosingScope(IRankScope newEnclosingScope) {
        if ((this.enclosingScope != null) && (newEnclosingScope != null)) {
            if (this.enclosingScope == newEnclosingScope) {
                return;
            }
            Log.warn("0xA1042 Scope \"" + getName() + "\" has already an enclosing scope.");
        }

        // remove this scope from current (old) enclosing scope, if exists.
        //#TODO only remove the subscope
        if (this.enclosingScope != null && enclosingScope.getSubScopes().contains(this)) {
            this.enclosingScope.removeSubScope(this);
        }

        // add this scope to new enclosing scope, if exists.
        if (newEnclosingScope != null) {
            newEnclosingScope.addSubScope(this);
        }

        // set new enclosing scope (or null)
        this.enclosingScope = newEnclosingScope;
    }
}
