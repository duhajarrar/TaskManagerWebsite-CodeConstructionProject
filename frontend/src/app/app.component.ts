import { Component, DoCheck, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from './services/storage/storage.service';
import { EventBusService } from './services/shared/event/event-bus.service';
import { Subscription } from 'rxjs';
import { environment } from './../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements DoCheck, OnInit {
  title = 'task-manager';
  ismenurequired = false;
  isLoggedIn = false;
  isAdmin = false;

  isExpanded = false;

  eventBusSub?: Subscription;

  constructor(
    private router: Router,
    private storage: StorageService,
    private eventBusService: EventBusService
  ) {
    console.log(environment.production); // Logs false for development environment
  }

  ngOnInit(): void {
    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.storage.clean();
    });
  }
  ngOnDestroy(): void {
    if (this.eventBusSub) this.eventBusSub.unsubscribe();
  }

  ngDoCheck(): void {
    let currenturl = this.router.url;
    if (currenturl == '/login' || currenturl == '/register') {
      this.ismenurequired = false;
    } else {
      this.ismenurequired = true;
    }
    if (this.storage.IsLoggedIn()) {
      this.isLoggedIn = true;
    } else {
      this.isLoggedIn = false;
    }
    if (this.storage.IsAdmin()) {
      this.isAdmin = true;
    } else {
      this.isAdmin = false;
    }
  }
  isSelected(routeName: string): boolean {
    // handling home case
    if (routeName !== '') {
      return this.router.url.includes(routeName);
    }
    if (this.router.url === '/') {
      return true;
    }
    return false;
  }
}
