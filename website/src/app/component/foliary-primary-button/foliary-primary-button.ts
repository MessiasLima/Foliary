import { Component } from '@angular/core';

@Component({
  selector: 'button[foliary-primary-button], a[foliary-primary-button]',
  standalone: true,
  templateUrl: './foliary-primary-button.html',
  styleUrl: './foliary-primary-button.css',
  host: {
    'class': 'bg-primary text-on-primary rounded-full px-6 py-3 font-medium'
  }
})
export class FoliaryPrimaryButton {
}
