package com.revolut.base.domain.executors

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}
