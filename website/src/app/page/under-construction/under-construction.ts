import { Component } from '@angular/core';
import { FoliaryCard } from '../../component/foliary-card/foliary-card';
import { FoliaryPrimaryButton } from '../../component/foliary-primary-button/foliary-primary-button';

@Component({
  selector: 'app-under-construction',
  standalone: true,
  imports: [
    FoliaryCard,
    FoliaryPrimaryButton,
  ],
  templateUrl: './under-construction.html',
  styleUrl: './under-construction.css',
})
export class UnderConstruction {}
