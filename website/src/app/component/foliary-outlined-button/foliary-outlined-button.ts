import { Component, ChangeDetectionStrategy } from '@angular/core';

@Component({
    selector: 'button[foliary-outlined-button], a[foliary-outlined-button]',
    standalone: true,
    templateUrl: './foliary-outlined-button.html',
    styleUrl: './foliary-outlined-button.css',
    changeDetection: ChangeDetectionStrategy.Eager,
    host: {
        class: 'bg-transparent text-primary border border-outline rounded-full px-6 py-3 font-medium',
    },
})
export class FoliaryOutlinedButton {}
