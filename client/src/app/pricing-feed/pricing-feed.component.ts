import { Component, OnInit } from '@angular/core';
import { ApiResponse } from '../model/api-response.model';
import { PricingFeedService } from '../services/pricing-feed.service';
import { PricingFeedId } from './pricing-feed-id.model';
import { PricingFeed } from './pricing-feed.model';
import { FormBuilder, FormGroup, Validators, FormControl  } from '@angular/forms';

@Component({
  selector: 'app-pricing-feed',
  templateUrl: './pricing-feed.component.html',
  styleUrls: ['./pricing-feed.component.css']
})
export class PricingFeedComponent implements OnInit {
  pricingFeedList: PricingFeed[] = [];
  selectedPricingFeed: PricingFeed | undefined;
  message: string | undefined;
  displayDialog: boolean = false;

  first: number = 0;
  rows: number = 10;
  totalRecords: number = 0;

  pricingFeedForm: FormGroup = new FormGroup({
    storeId: new FormControl(),
    sku: new FormControl(),
    productName: new FormControl(),
    price: new FormControl(),
    date: new FormControl()
  });
  pricingFeed?: PricingFeed;

  file: File | null = null;

  constructor(
    private pricingFeedService: PricingFeedService,
    private fb: FormBuilder
  ){
    this.getTotalCount();
  }

  ngOnInit() {
    this.loadPricingFeedList(this.first, this.rows);
  }

  getTotalCount() {
    this.pricingFeedService.getCount().subscribe(
      (response: ApiResponse<number>) => {
        this.totalRecords = response.data;
      },
      (error: any) => {
        console.log(error.message);
      }
    )
  }

  loadPricingFeedList(first: number, rows: number) {
    // Call the findAll method to get all pricing feeds
    this.pricingFeedService.findAll(first, rows).subscribe(
      (response: ApiResponse<PricingFeed[]>) => {
        this.pricingFeedList = response.data;
      },
      (error: any) => {
        console.log(error.message);
      }
    );
  }

  savePricingFeed() {
    const formValue = this.pricingFeedForm.value;
    // Map the form values to the pricingFeed object
    if(this.pricingFeed != undefined) {
      const pricingFeedId = new PricingFeedId();
      pricingFeedId.storeId = formValue.storeId;
      pricingFeedId.sku = formValue.sku;
      this.pricingFeed.id = pricingFeedId;
      this.pricingFeed.productName = formValue.productName;
      this.pricingFeed.price = formValue.price;
      this.pricingFeed.date = formValue.date;

      // Call the create method to create a new pricing feed
      this.pricingFeedService.create(this.pricingFeed).subscribe(
        (response: ApiResponse<PricingFeed>) => {
          this.pricingFeedList.push(response.data);
          this.displayDialog = false;
        },
        (error: any) => {
          console.log(error.message);
        }
      );
    }
    
  }

  onPricingFeedAdd() {
    this.pricingFeed = new PricingFeed();
    this.displayDialog = true;
  }

  onRowEditInit(pricingFeed: PricingFeed) {
    this.selectedPricingFeed = pricingFeed;
    console.log("selected pricing feed", this.selectedPricingFeed);
  }

  onRowEditSave() {
    console.log("On edit", this.selectedPricingFeed);
    if(this.selectedPricingFeed) {
      const storeId = this.selectedPricingFeed?.id?.storeId;
      const sku = this.selectedPricingFeed?.id?.sku;
      if(storeId != undefined && sku != undefined) {
        // Call the update method to update the selected pricing feed
        this.pricingFeedService.update(storeId, sku, this.selectedPricingFeed).subscribe(
          (response: ApiResponse<PricingFeed>) => {
            const index = this.pricingFeedList.findIndex(p => p.id?.storeId === this.selectedPricingFeed?.id?.storeId 
              && p.id?.sku === this.selectedPricingFeed?.id?.sku);
            this.pricingFeedList[index] = response.data;
            this.selectedPricingFeed = response.data;
          },
          (error: any) => {
            this.message = error.message;
          }
        );
      }
    } else {
      alert("Please select any row");
    }
  }

  onRowEditCancel() {
    this.selectedPricingFeed = undefined;
  }

  deletePricingFeed(pricingFeed: PricingFeed) {
    console.log('call to delete pricing feed');
    const storeId = pricingFeed?.id?.storeId;
    const sku = pricingFeed?.id?.sku;
    if(storeId != undefined && sku != undefined) {
      // Call the delete method to delete the selected pricing feed
      this.pricingFeedService.delete(storeId, sku).subscribe(
        (response: ApiResponse<string>) => {
          this.loadPricingFeedList(this.first, this.rows);
          alert(response.data);
        },
        (error: any) => {
          console.log(error.message);
        }
      );
    }
      
  }

  onPageChange(event: any) {
    this.first = event.page;
    this.loadPricingFeedList(this.first, this.rows);
  }

  onFileUpload(event: any) {
    this.file = event.files[0];
    if (this.file) {
      this.pricingFeedService.import(this.file).subscribe(
        (response) => {
          console.log('File uploaded successfully.');
          this.loadPricingFeedList(this.first, this.rows);
          this.getTotalCount();
        },
        (error) => {
          console.error('Error uploading file:', error);
          this.loadPricingFeedList(this.first, this.rows);
          this.getTotalCount();
        }
      );
    }
  }

  applyFilterOnStoreId(event: any) {
    const filteredValue = event.target.value;
    console.log("applyFilterOnStoreId", filteredValue);
  }

  applyFilterOnSku(event: any) {
    const filteredValue = event.target.value;
    console.log("applyFilterOnSku", filteredValue);
  }

  applyFilterOnProductName(event: any) {
    const filteredValue = event.target.value;
    console.log("applyFilterOnProductName", filteredValue);
  }

  applyFilterOnPrice(event: any) {
    const filteredValue = event.target.value;
    console.log("applyFilterOnPrice", filteredValue);
  }

  applyFilterOnDate(event: any) {
    const filteredValue = event.target.value;
    console.log("applyFilterOnDate", filteredValue);
  }

}


