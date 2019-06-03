import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IOwner, Owner } from 'app/shared/model/owner.model';
import { OwnerService } from './owner.service';

@Component({
  selector: 'jhi-owner-update',
  templateUrl: './owner-update.component.html'
})
export class OwnerUpdateComponent implements OnInit {
  owner: IOwner;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    firstname: [],
    lastname: [],
    birthyear: []
  });

  constructor(protected ownerService: OwnerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ owner }) => {
      this.updateForm(owner);
      this.owner = owner;
    });
  }

  updateForm(owner: IOwner) {
    this.editForm.patchValue({
      id: owner.id,
      firstname: owner.firstname,
      lastname: owner.lastname,
      birthyear: owner.birthyear
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const owner = this.createFromForm();
    if (owner.id !== undefined) {
      this.subscribeToSaveResponse(this.ownerService.update(owner));
    } else {
      this.subscribeToSaveResponse(this.ownerService.create(owner));
    }
  }

  private createFromForm(): IOwner {
    const entity = {
      ...new Owner(),
      id: this.editForm.get(['id']).value,
      firstname: this.editForm.get(['firstname']).value,
      lastname: this.editForm.get(['lastname']).value,
      birthyear: this.editForm.get(['birthyear']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOwner>>) {
    result.subscribe((res: HttpResponse<IOwner>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
