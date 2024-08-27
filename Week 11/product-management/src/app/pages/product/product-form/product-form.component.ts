import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss'],
})
export class ProductFormComponent implements OnInit {
  @Input() product: Product | null = null;
  @Output() save = new EventEmitter<Product>();
  @Output() cancel = new EventEmitter<void>();

  productForm: FormGroup = this.fb.group({
    id: ['', Validators.required],
    name: ['', Validators.required],
    price: ['', Validators.required],
    status: ['ACTIVE', Validators.required],
  });
  isVisible = true;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.productForm = this.fb.group({
      id: [{ value: this.product?.id || '', disabled: !!this.product }, Validators.required],
      name: [this.product?.name || '', Validators.required],
      price: [this.product?.price || '', Validators.required],
      status: [this.product?.status || 'ACTIVE', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.getRawValue();
      if (this.product) {
        productData.id = this.product.id;
      }
      this.save.emit(productData);
      this.onClose();
    }
  }

  onClose(): void {
    this.isVisible = false;
    this.cancel.emit();
  }
}
