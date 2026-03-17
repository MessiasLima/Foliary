import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavigationBar } from './component/navigation-bar/navigation-bar';
import { Footer } from './component/footer/footer';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavigationBar, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
