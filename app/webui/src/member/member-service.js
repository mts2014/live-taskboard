import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';
import {AuthContext} from '../auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class MemberService {
  _members = [];

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  loadByGroup(groupId) {
    return this.httpLoader.list(
      `/api/task-manage/groups/${groupId}/members/`,
      response => {
        return response.content.data.members;
      });
  }

  changeToAdmin(groupId, member) {
    if (member.admin) return;

    this.http.call(http => {
      return http
        .put(`/api/task-manage/groups/${groupId}/members/${member.memberId}`, { admin: true })
        .then(response => {
          member.admin = true;
          this.eventAggregator.publish(new MemberRoleChanged());
        });
    }, true);
  }
  changeToNormal(groupId, member) {
    if (!member.admin) return;

    this.http.call(http => {
      return http
        .put(`/api/task-manage/groups/${groupId}/members/${member.memberId}`, { admin: false })
        .then(response => {
          member.admin = false;
          this.eventAggregator.publish(new MemberChanged());
        });
    }, true);
  }
  remove(groupId, member) {
    member.removed = true;

    this.http.call(http => {
      return http
        .delete(`/api/task-manage/groups/${groupId}/members/${member.memberId}`)
        .then(response => {
          this.eventAggregator.publish(new MemberRemoved());
        });
    }, true);
  }
}

export class MemberChanged {}
export class MemberRemoved {}
