/*
 * This file is part of muCommander, http://www.mucommander.com
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mucommander.ui.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

/**
 * Focus traversal policy for {@link MainFrame}: routes the Tab key around the
 * left/right folder panel pair (location text fields, file tables, and tree
 * sub-components) in a stable order.
 *
 * <p>Extracted from {@code MainFrame} as the first step of the Phase 5.2
 * decomposition. Behaviour identical to the historical
 * {@code MainFrame.CustomFocusTraversalPolicy} inner class — see the
 * package-level test {@code MainFrameFocusTraversalPolicyTest}.
 *
 * @author Maxence Bernard
 */
class MainFrameFocusTraversalPolicy extends FocusTraversalPolicy {

    private final MainFrame mainFrame;

    MainFrameFocusTraversalPolicy(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public Component getComponentAfter(Container container, Component component) {
        FolderPanel left = mainFrame.getLeftPanel();
        FolderPanel right = mainFrame.getRightPanel();
        Component leftTable = left.getFileTable();
        Component rightTable = right.getFileTable();

        if (component == left.getFoldersTreePanel().getTree()) {
            return leftTable;
        }
        if (component == right.getFoldersTreePanel().getTree()) {
            return rightTable;
        }
        if (component == left.getLocationTextField()) {
            return leftTable;
        }
        if (component == leftTable) {
            return rightTable;
        }
        if (component == right.getLocationTextField()) {
            return rightTable;
        }
        // otherwise (component == rightTable)
        return leftTable;
    }

    @Override
    public Component getComponentBefore(Container container, Component component) {
        // Completely symmetrical with getComponentAfter
        return getComponentAfter(container, component);
    }

    @Override
    public Component getFirstComponent(Container container) {
        return mainFrame.getLeftPanel().getFileTable();
    }

    @Override
    public Component getLastComponent(Container container) {
        return mainFrame.getRightPanel().getFileTable();
    }

    @Override
    public Component getDefaultComponent(Container container) {
        return mainFrame.getActiveTable();
    }
}
