import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavigationBar } from './component/navigation-bar/navigation-bar';
import { Footer } from './component/footer/footer';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, NavigationBar, Footer],
    templateUrl: './app.html',
    changeDetection: ChangeDetectionStrategy.Eager,
    styleUrl: './app.css',
})
export class App {}
