import _ from 'underscore';
import {customElement, inject, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import bootbox from 'bootbox';
import {TaskService, TaskRemoved, TaskModified, TasksLoaded} from './task/task-service';
import {MemberService} from './member/member-service';
import {AuthContext} from './auth/auth-context';
import 'components/jqueryui';
import 'components/jqueryui/themes/base/jquery-ui.css!';


@customElement('task-status')
@inject(EventAggregator, TaskService, MemberService, AuthContext)
export class TaskStatus {
  @bindable status = '';
  _tasks = {};
  members = [];

  constructor(eventAggregator, taskService, memberService, authContext) {
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.taskService = taskService;
    this.memberService = memberService;
    this.vm = this;
    this.authContext = authContext;
  }

  showMemoEdit(task) {
    this.events.publish('task-memo-edit.init', [ this.group.groupId, task.taskId ]);
    $(this.taskMemoEditModel).modal('show');
  }

  removeTask(task) {
    bootbox.confirm('削除します。よろしいですか？', confirmation => {
      if(confirmation) {
        this.taskService.remove(this.group.groupId, task.taskId);
      }
    });
  }

  loadTasks() {
    this._tasks = this.taskService.load(this.group.groupId);
  }

  modifyTask(task) {
    this.taskService.modify(this.group.groupId, task);
  }

  changeStatus(taskId) {
    this.taskService.changeStatus(this.group.groupId, taskId, this.status);
  }

  deadlineCss(deadline) {
    if (!deadline) return '';

    const now = new Date();
    const nowTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const deadlineTime = new Date(`${deadline} 00:00:00`).getTime();
    if (nowTime > deadlineTime) {
      return 'task-deadline-error';
    }
    if (deadlineTime - nowTime <= 3 * 24 * 60 * 60 * 1000) {
      return 'task-deadline-warn';
    }
    return '';
  }

  assigned(assignedMemberId) {
    return assignedMemberId === this.authContext.getUserId();
  }

  get tasks() {
    return this._tasks[this.status] || [];
  }

  bind(bindingContext) {
    //this.taskboardContext = bindingContext.taskboardContext;
  }

  attached() {

    this.events.subscribe('group.selected', group => {
      this.group = group;
      this.members = this.memberService.loadByGroup(group.groupId);
      this.taskService.watchTaskChange(this.group.groupId, (taskChange, self) => {
        if(!self) this.loadTasks();
      });
      this.loadTasks();
    });
    this.events.subscribe2(['task-register.register.success', TaskRemoved], () => {
      this.loadTasks();
    });
    this.events.subscribe(TasksLoaded, () => {
      this.events.publish('task.loaded', this.group.groupId);
    });
    this.events.subscribe('taskName.changed', args => {
      const task = args[0];
      if (this._hasTask(task)) {
        task.taskName = args[1];
        this.modifyTask(task);
      }
    });

  }

  _hasTask(task) {
    return !! _.find(this._tasks[this.status], (t) => {
      return t.taskId === task.taskId;
    });
  }

}

