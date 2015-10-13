import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

@inject(HttpClient, EventAggregator)
export class UserService {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  register(user) {

    this.http
      .post('/api/auth-access/users/', user)
      .then(response => {
        this.eventAggregator.publish(new UserRegistered());
      })
      .catch(response => {
        this.eventAggregator.publish('user.register.failed', new GlobalError(response.content.errors));
      });
  }

  validate(user) {

    this.http
      .post('/api/auth-access/users/?validate', user)
      .then(response => {
        this.eventAggregator.publish(new UserRegisterValidationSuccess());
      })
      .catch(response => {
        this.eventAggregator.publish(new UserRegisterValidationError(new GlobalError(response.content.errors)));
      });
  }
}
export class UserRegistered {}
export class UserRegisterValidationError {
  constructor(error){
    this.error = error;
  }
}
export class UserRegisterValidationSuccess {}