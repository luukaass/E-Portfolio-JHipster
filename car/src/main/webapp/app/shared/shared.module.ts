import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CarSharedLibsModule, CarSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [CarSharedLibsModule, CarSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [CarSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CarSharedModule {
  static forRoot() {
    return {
      ngModule: CarSharedModule
    };
  }
}
