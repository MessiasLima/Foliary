import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-navigation-bar',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './navigation-bar.html',
    changeDetection: ChangeDetectionStrategy.Eager,
    styleUrl: './navigation-bar.css',
})
export class NavigationBar {}
