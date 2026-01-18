import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-adminaddproduct',
  templateUrl: './adminaddproduct.component.html',
  styleUrls: ['./adminaddproduct.component.css']
})
export class AdminaddproductComponent {

  productForm!: FormGroup;
  isSubmit: boolean = false;
  photoImageUrl: string = '';

  constructor(private service: ProductService, private router: Router, private fb: FormBuilder) {}

  
ngOnInit(): void {
  this.productForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(60)]],
    description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
    price: [null, [Validators.required, Validators.min(1)]],
    stock: [null, [Validators.required, Validators.min(5)]], // min 15
    category: ['', Validators.required],
    photoImage: ['', Validators.required]
  });
}

  addProduct() {
    this.isSubmit = true;
    if (this.productForm.valid) {
      this.service.addProducts(this.productForm.value).subscribe(data => {
        console.log(data);
        this.router.navigate(['admin/view-products']);
      });
    }
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.photoImageUrl = e.target.result; // base64 string
        this.productForm.patchValue({ photoImage: this.photoImageUrl });
      };
      reader.readAsDataURL(file);
    }
  }
}