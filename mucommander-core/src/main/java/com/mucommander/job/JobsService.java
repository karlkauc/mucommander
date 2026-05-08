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
package com.mucommander.job;

import java.util.List;

import com.mucommander.commons.file.AbstractFile;

/**
 * Read/write contract for job tracking, extracted from {@link JobsManager} so
 * consumers can be wired to alternative implementations (e.g. a test fake)
 * instead of always reaching for {@code JobsManager.getInstance()}.
 *
 * <p>The {@link FileJobListener} callbacks that {@code JobsManager} also
 * implements are intentionally left off this interface — they're internal
 * job-side wiring, not part of what callers consume.
 */
public interface JobsService {

    /** Registers a listener notified whenever job state changes. */
    void addJobListener(JobListener l);

    /** Unregisters a previously-added listener. */
    void removeJobListener(JobListener l);

    /** Adds a new job, starts tracking its progress, and notifies listeners. */
    void addJob(FileJob job);

    /** Returns the number of jobs currently tracked (running, paused, or finished but still displayed). */
    int getJobCount();

    /**
     * Returns true if any tracked job has the given folder as a destination
     * and might mutate it (used to suppress folder-change auto-refresh while
     * a copy/move/delete is in flight).
     */
    boolean mayFolderChangeByExistingJob(AbstractFile folder);

    /** Called by a job thread when it finishes; lets the manager remove it after a delay. */
    void jobEnded(FileJob job);

    /** Snapshot of jobs currently flagged as background-mode. */
    List<FileJob> getBackgroundJobs();

    /** Snapshot of all tracked jobs (background and foreground). */
    List<FileJob> getAllJobs();
}
