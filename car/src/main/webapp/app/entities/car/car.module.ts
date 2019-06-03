import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CarSharedModule } from 'app/shared';
import {
  CarComponent,
  CarDetailComponent,
  CarUpdateComponent,
  CarDeletePopupComponent,
  CarDeleteDialogComponent,
  carRoute,
  carPopupRoute
} from './';

const ENTITY_STATES = [...carRoute, ...carPopupRoute];

@NgModule({
  imports: [CarSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CarComponent, CarDetailComponent, CarUpdateComponent, CarDeleteDialogComponent, CarDeletePopupComponent],
  entryComponents: [CarComponent, CarUpdateComponent, CarDeleteDialogComponent, CarDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CarCarModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
