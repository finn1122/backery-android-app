<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class SaleResource extends JsonResource
{
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'branchId' => $this->branch_id,
            'userId' => $this->user_id,
            'total' => $this->total,
            'createdAt' => $this->created_at,
            'updatedAt' => $this->updated_at,
            'active' => $this->active,
        ];
    }
}
