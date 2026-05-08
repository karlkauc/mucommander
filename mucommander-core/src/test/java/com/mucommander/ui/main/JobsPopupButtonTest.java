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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import com.mucommander.job.FileJob;
import com.mucommander.job.JobsService;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link JobsPopupButton}'s wiring to its
 * {@link JobsService} dependency.
 *
 * The test-only constructor lets us substitute a mock in instead of going
 * through {@code JobsManager.getInstance()}; we can then verify the button
 * subscribes itself as a listener on construction and queries the service
 * for the running-jobs count when jobs come and go.
 */
public class JobsPopupButtonTest {

    @Test
    public void constructor_subscribesToJobsService() {
        JobsService service = mock(JobsService.class);

        JobsPopupButton button = new JobsPopupButton(service);

        verify(service, times(1)).addJobListener(button);
    }

    @Test
    public void constructor_startsHidden() {
        JobsService service = mock(JobsService.class);

        JobsPopupButton button = new JobsPopupButton(service);

        assertFalse(button.isVisible(),
                "JobsPopupButton must be hidden until at least one background job is running");
    }

    @Test
    public void jobAdded_makesButtonVisible_whenServiceReportsBackgroundJobs() {
        JobsService service = mock(JobsService.class);
        when(service.getBackgroundJobs()).thenReturn(List.of(mock(FileJob.class)));
        JobsPopupButton button = new JobsPopupButton(service);

        button.jobAdded(mock(FileJob.class));

        assertTrue(button.isVisible(),
                "Button must become visible once the service reports a background job");
    }

    @Test
    public void jobAdded_keepsButtonHidden_whenServiceReportsNoBackgroundJobs() {
        JobsService service = mock(JobsService.class);
        when(service.getBackgroundJobs()).thenReturn(Collections.emptyList());
        JobsPopupButton button = new JobsPopupButton(service);

        button.jobAdded(mock(FileJob.class));

        assertFalse(button.isVisible());
    }

    @Test
    public void jobRemoved_hidesButton_whenLastBackgroundJobGone() {
        JobsService service = mock(JobsService.class);
        // First make it visible by simulating a job-added with one running job.
        when(service.getBackgroundJobs()).thenReturn(List.of(mock(FileJob.class)));
        JobsPopupButton button = new JobsPopupButton(service);
        button.jobAdded(mock(FileJob.class));
        assertTrue(button.isVisible());

        // Then drain the background list and signal removal.
        when(service.getBackgroundJobs()).thenReturn(Collections.emptyList());
        button.jobRemoved(mock(FileJob.class));

        assertFalse(button.isVisible(),
                "Button must hide again once no background jobs remain");
    }
}
