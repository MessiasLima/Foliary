import { Component } from '@angular/core';
import { FoliaryCard } from '../../component/foliary-card/foliary-card';
import { FoliaryPrimaryButton } from '../../component/foliary-primary-button/foliary-primary-button';
import { FoliarySecondaryButton } from '../../component/foliary-secondary-button/foliary-secondary-button';
import { FoliaryOutlinedButton } from '../../component/foliary-outlined-button/foliary-outlined-button';

@Component({
  selector: 'app-under-construction',
  standalone: true,
  imports: [
    FoliaryCard,
    FoliaryPrimaryButton,
    FoliarySecondaryButton,
    FoliaryOutlinedButton,
  ],
  templateUrl: './under-construction.html',
  styleUrl: './under-construction.css',
})
export class UnderConstruction {}
