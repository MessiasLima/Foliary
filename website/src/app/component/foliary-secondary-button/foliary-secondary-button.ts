import { Component } from '@angular/core';

@Component({
    selector: 'button[foliary-secondary-button], a[foliary-secondary-button]',
    standalone: true,
    templateUrl: './foliary-secondary-button.html',
    styleUrl: './foliary-secondary-button.css',
    host: {
        class: 'bg-secondary-container text-on-secondary-container rounded-full px-6 py-3 font-medium',
    },
})
export class FoliarySecondaryButton {}
