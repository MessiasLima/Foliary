import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-footer',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './footer.html',
    changeDetection: ChangeDetectionStrategy.Eager,
    styleUrl: './footer.css',
})
export class Footer {}
