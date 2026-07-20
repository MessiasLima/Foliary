import { Component, ChangeDetectionStrategy } from '@angular/core';
import { FoliaryCard } from '../../component/foliary-card/foliary-card';
import { FoliaryPrimaryButton } from '../../component/foliary-primary-button/foliary-primary-button';

@Component({
    selector: 'app-under-construction',
    standalone: true,
    imports: [FoliaryCard, FoliaryPrimaryButton],
    templateUrl: './under-construction.html',
    changeDetection: ChangeDetectionStrategy.Eager,
    styleUrl: './under-construction.css',
})
export class UnderConstruction {}
