import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Address } from 'src/app/models/address.model';
import { AddressService } from 'src/app/services/address.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-useraddress',
  templateUrl: './useraddress.component.html',
  styleUrls: ['./useraddress.component.css']
})
export class UseraddressComponent implements OnInit {
  @Input() addresses: Address[] = [];
  // @Output() addressesChange = new EventEmitter<Address[]>();

  @Output() addressSelected = new EventEmitter<Address>();
  userId:Number = null;

  selectedAddress: Address | null = null;
  editingAddress: Address | null = null;
  isAddingNew = false;
  selectedAddressId: Number | null = null;

  // Form model for add/edit
  formAddress: Address = {
    addressData: '',
    pincode: '',
    category: 'Home'
  };

  categories = ['Home', 'Work', 'Other'];

  constructor(private userState:UserStateService,private addressService:AddressService) {}

  ngOnInit(): void {
    this.userState.user$.subscribe({
      next:(user) => {
        this.userId = user.userId;
      },
      error:(err) => console.log(err)
    });
  }

  // Select an address (for parent component to know which is active)
  selectAddress(address: Address) {
    this.selectedAddress = address;
    
    this.addressSelected.emit(address);
  }

  // Start editing
  editAddress(address: Address) {
    this.editingAddress = { ...address };
    this.formAddress = { ...address };
    this.selectedAddressId = address.addressId;
    this.isAddingNew = false;
  }

  // Start adding new
  addNewAddress() {
    this.isAddingNew = true;
    this.editingAddress = null;
    this.formAddress = {
      addressData: '',
      pincode: '',
      category: 'Home'
    };
  }

  // Save (add or update)
  saveAddress() {
    if (!this.formAddress.addressData?.trim() || !this.formAddress.pincode?.trim()) {
      return; // simple validation
    }

    if (this.isAddingNew) {
      //ADD NEW
      this.addressService.addAddress(this.userId, this.formAddress).subscribe({
        next:(data) => {
          this.userState.refreshUser();
        }
      })
    } else if (this.editingAddress) {
      // Update existing
      this.addressService.updateAddress(this.selectedAddressId,this.formAddress).subscribe({
        next:data => {
          this.userState.refreshUser();
        },
        error:(err)=>console.log(err)
      })
    }

    this.cancelEdit();
  }

  // Delete address
  deleteAddress(address: Address) {
    this.addressService.deleteAddress(address.addressId).subscribe(data => {
      this.userState.refreshUser();
    })
    if (this.selectedAddress?.addressId === address.addressId) {
      this.selectedAddress = null;
      this.addressSelected.emit(null);
    }
  }

  // Cancel add/edit
  cancelEdit() {
    this.isAddingNew = false;
    this.editingAddress = null;
    this.selectedAddressId = null;
    this.formAddress = { addressData: '', pincode: '', category: 'Home' };
  }

}
