import { Component } from '@angular/core';

@Component({
  selector: 'foliary-card',
  standalone: true,
  templateUrl: './foliary-card.html',
  styleUrl: './foliary-card.css',
  host: {
    'class': 'bg-surface text-on-surface border border-outline rounded-xl'
  }
})
export class FoliaryCard {
}
